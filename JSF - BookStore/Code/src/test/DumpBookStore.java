/*
 * DumpBookStore.java
 */

package test;

import org.hibernate.*;
import org.hibernate.cfg.*;

import hibernate.HibernateUtil;

import java.util.*;
import model.*;

public class DumpBookStore {
  public static void main(String[] args) throws Exception {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Category> categories = session.createQuery("from model.Category").list();
    for (Category category : categories) {
      System.out.println(category.getTitle());
      for(Book book : category.getBooks()) {
        System.out.println(book.getTitle());
        System.out.println(book.getPrice());
        System.out.println(book.getDate());
        for(Author author : book.getAuthors()) {
          System.out.println(author.getLastName());
        }
      }
    }
    session.getTransaction().commit();
  }

}
