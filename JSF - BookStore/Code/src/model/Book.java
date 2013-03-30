package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity()
public class Book extends Persistent {
	private String title = "";
	private Category category;
	private java.math.BigDecimal price = new BigDecimal(0);;
	private java.util.Date date = new Date();
	private List<Author> authors;
	private byte[] photo;
	private List<OrderItem> orderItems = new ArrayList();;
	private int achete;


	public Book() {
		authors = new ArrayList();
	}

	public boolean equals(Object other){
		if(other != null && other instanceof Book)
			return getTitle().equals(((Book)other).getTitle());
		return false;
	}
	public int hashCode(){
		return getTitle().hashCode();
	}

	public String toString() {
		return "Id:" + getId() + " Title:" + getTitle() + " Price:" + getPrice() + " Category:" + getCategory() + " Authors:" + getAuthors();
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne()
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public java.math.BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.util.Date getDate() {
		return this.date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	@ManyToMany(targetEntity = model.Author.class)
	@JoinTable(name = "BookAuthor", joinColumns = { @JoinColumn(name = "bookId") }, inverseJoinColumns = { @JoinColumn(name = "authorId") })
	public List<Author> getAuthors() {
		return this.authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void addAuthor(Author author) {
		authors.add(author);
	}

	public void removeAuthor(Author author) {
		authors.remove(author);
	}

	@Lob
	@Column(length = 100000)
	// La taille de la colonne est nécessaire pour Derby
	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@OneToMany(mappedBy = "book")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public int getAchete() {
		return achete;
	}

	public void setAchete(int achete) {
		this.achete = achete;
	}

	public void incAchete(){
		this.achete++;
	}

	public void incAchete(int nbr){
		this.achete += nbr;
	}

}
