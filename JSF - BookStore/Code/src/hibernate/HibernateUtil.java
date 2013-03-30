package hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * Basic Hibernate helper class for Hibernate configuration and startup.
 * <p>
 * Uses a static initializer to read startup options and initialize
 * <tt>Configuration</tt> and <tt>SessionFactory</tt>.
 * <p>
 */
public class HibernateUtil {

	private static Log log = LogFactory.getLog(HibernateUtil.class);


	private static Configuration configuration;

	private static SessionFactory sessionFactory;

	static {
		try {
			configuration = new AnnotationConfiguration();

			configuration.configure();
			sessionFactory = configuration.buildSessionFactory();

		} catch (Throwable ex) {
			log.error("Building SessionFactory failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns the original Hibernate configuration.
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the global SessionFactory.
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		SessionFactory sf = sessionFactory;
		if (sf == null)
			throw new IllegalStateException("SessionFactory not available.");
		return sf;
	}

	/**
	 * Closes the current SessionFactory and releases all resources.
	 * <p>
	 * The only other method that can be called on HibernateUtil after this one
	 * is rebuildSessionFactory(Configuration).
	 */
	public static void shutdown() {
		log.debug("Shutting down Hibernate.");
		getSessionFactory().close();
		configuration = null;
		sessionFactory = null;
	}
}
