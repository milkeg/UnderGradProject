package hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.context.ManagedSessionContext;

/*
 * Filter that manages a Hibernate Session for a conversation.
 */
public class HibernateSessionConversationFilter implements Filter {

	protected static ThreadLocal<Integer> conversationFlag;

	private static final int START_CONVERSATION = 1;

	private static final int END_CONVERSATION = 2;

	private static Log log = LogFactory.getLog(HibernateSessionConversationFilter.class);

	private SessionFactory sf;

	public static final String HIBERNATE_SESSION_KEY = "hibernateSession";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		org.hibernate.classic.Session currentSession;

		// Try to get a Hibernate Session from the HttpSession
		HttpSession httpSession = ((HttpServletRequest) request).getSession();
		Session disconnectedSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);
		boolean inConversation = false;

		try {

			log.debug("Entering filter for " + ((HttpServletRequest) request).getRequestURL());

			// Start a new conversation or in the middle?
			if (disconnectedSession == null) {
				log.debug(">>> New conversation");
				log.debug("Opening Session, disabling automatic flushing");
				currentSession = sf.openSession();
				currentSession.setFlushMode(FlushMode.MANUAL);
			} else {
				log.debug("< Continuing conversation");
				currentSession = (org.hibernate.classic.Session) disconnectedSession;
				inConversation = true;
			}

			log.debug("Binding the current Session");
			ManagedSessionContext.bind(currentSession);

			log.debug("Starting a database transaction");
			currentSession.beginTransaction();

			chain.doFilter(request, response);

			// End or continue the long-running conversation?
			boolean startConversation = false;
			boolean endConversation = false;
			if (conversationFlag != null) {
				int conversationState = conversationFlag.get().intValue();
				endConversation = conversationState == END_CONVERSATION;
				startConversation = conversationState == START_CONVERSATION;
			}

			if (inConversation && !endConversation || !inConversation && startConversation) {
				if (currentSession.getTransaction().isActive()) {
					log.debug("Committing database transaction");
					currentSession.getTransaction().commit();
				}
				log.debug("Unbinding Session after processing");
				currentSession = ManagedSessionContext.unbind(sf);

				log.debug("Storing Session in the HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);

				log.debug("> Returning to user in conversation");

			} else {
				/*
				 * Le flush doit etre fait ou non par l'application log.debug("Flushing
				 * Session"); currentSession.flush();
				 */
				if (currentSession.getTransaction().isActive()) {
					log.debug("Committing the database transaction");
					currentSession.getTransaction().commit();
				}
				log.debug("Unbinding Session after processing");
				currentSession = ManagedSessionContext.unbind(sf);

				log.debug("Closing the Session");
				currentSession.close();

				log.debug("Cleaning Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

				log.debug("<<< End of conversation");

			}

		} catch (StaleObjectStateException staleEx) {
			log.error("This interceptor does not implement optimistic concurrency control!");
			log.error("Your application will not work until you add compensation actions!");
			throw staleEx;
		} catch (Throwable ex) {
			// Rollback only
			try {
				if (sf.getCurrentSession().getTransaction().isActive()) {
					log.debug("Trying to rollback database transaction after exception");
					sf.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				log.error("Could not rollback transaction after exception!", rbEx);
			} finally {
				log.error("Cleanup after exception!");

				// Cleanup
				log.debug("Unbinding Session after exception");
				currentSession = ManagedSessionContext.unbind(sf);

				log.debug("Closing Session after exception");
				currentSession.close();

				log.debug("Removing Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

			}
			throw new ServletException(ex);
		}
		conversationFlag = null;
		log.debug("Exiting filter for " + ((HttpServletRequest) request).getRequestURL());
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initializing filter...");
		log.debug("Obtaining SessionFactory from HibernateUtil");
		sf = HibernateUtil.getSessionFactory();
	}

	public void destroy() {
	}

	public static void startConversation() {
		conversationFlag = new ThreadLocal<Integer>();
		conversationFlag.set(new Integer(START_CONVERSATION));
	}

	public static void endConversation() {
		conversationFlag = new ThreadLocal<Integer>();
		conversationFlag.set(new Integer(END_CONVERSATION));
	}

}
