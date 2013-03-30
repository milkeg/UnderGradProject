package test;

import hibernate.HibernateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import model.Author;
import model.Book;
import model.Category;
import model.Client;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class CreateBookStore {

  public static void main(String[] args) throws Exception {

    Configuration configuration = new AnnotationConfiguration().configure();
    SchemaExport export = new SchemaExport(configuration);
    export.setOutputFile("ddl/bookstore.ddl");
    export.create(true, true);

     Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();

    Date now = new Date();

    Category category1 = new Category();
    category1.setTitle("Informatique");
    session.save(category1);

    Category category2 = new Category();
    category2.setTitle("Littérature");
    session.save(category2);

    Author author1 = new Author();
    author1.setFirstName("Michel");
    author1.setLastName("Martin");
    session.save(author1);

    Author author2 = new Author();
    author2.setFirstName("Christian");
    author2.setLastName("Bauer");
    session.save(author2);

    Author author3 = new Author();
    author3.setFirstName("J-K");
    author3.setLastName("Rowling");
    session.save(author3);

    
    Book book = new Book();
    book.setTitle("HTML 5 et CSS 3");
    book.setCategory(category1);
    book.setPrice(new java.math.BigDecimal("10.50"));
    book.setDate(now);
    book.addAuthor(author1);
    book.setAchete(0);
    File f = new File("WebContent/resources/images/html.jpg");
    byte[] photo = new byte[(int)(f.length())];
    FileInputStream fi = new FileInputStream(f);
    fi.read(photo);
    book.setPhoto(photo);
    session.save(book);

    book = new Book();
    book.setTitle("Hibernate in action");
    book.setCategory(category1);
    book.setPrice(new java.math.BigDecimal("30.50"));
    book.setDate(now);
    book.addAuthor(author1);
    book.addAuthor(author2);
    book.setAchete(0);
    f = new File("WebContent/resources/images/hibernate.jpg");
    photo = new byte[(int)(f.length())];
    fi = new FileInputStream(f);
    fi.read(photo);
    book.setPhoto(photo);
    session.save(book);
     
    book = new Book();
    book.setTitle("Harry Potter");
    book.setCategory(category2);
    book.setPrice(new java.math.BigDecimal("20.50"));
    book.setDate(now);
    book.addAuthor(author3);
    book.setAchete(0);
    f = new File("WebContent/resources/images/harry.jpg");
    photo = new byte[(int)(f.length())];
    fi = new FileInputStream(f);
    fi.read(photo);
    book.setPhoto(photo);
    session.save(book);

    Client user = new Client();
    user.setLogin("galland");
    user.setPassword("dauphine");
    user.setEmail("dominique.galland@yopmail.com");
    user.setValidate("true");
    user.setRemember(false);
    user.setPhoto(null);
    Date time = new Date();
    user.setLastlogin(time.getTime());
    session.save(user);
    
    session.getTransaction().commit();
  }

}
