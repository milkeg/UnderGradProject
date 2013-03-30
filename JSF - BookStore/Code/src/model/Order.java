/**
 * 
 */
package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author dga
 *
 */
@Entity
@Table(name="Orders")  //Order est un mot réservé en SQL
public class Order extends Persistent{
	private Client client;
	private List<OrderItem> items = new ArrayList<OrderItem>();
	private BigDecimal total;
	private Date date;

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@ManyToOne
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	@OneToMany(mappedBy="order", cascade ={CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	@Transient
	public int getSize() {
		return items.size();
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	private void computeTotal(){
		total = new BigDecimal(0);
		for(OrderItem item : items){
			item .computeTotal();
			total = total.add(item.getTotal());
		}
	}

	private void addItem(OrderItem item){
		items.add(item);
		item.setOrder(this);
	} 

	private OrderItem find(Book book) {
		OrderItem found = null;
		for (Iterator<OrderItem> it = items.iterator(); it.hasNext()
				&& found == null;) {
			OrderItem item = it.next();
			if (book.equals(item.getBook())) {
				found = item;
			}
		}
		return found;
	}


	public void addOne(Book book) {
		OrderItem found = find(book);
		if (found == null) {
			addItem(new OrderItem(book, 1));
		} else {
			found.addOne();
		}
		computeTotal();
	}

	public void removeOne(Book book) {
		OrderItem found = find(book);
		if (found != null) {
			found.removeOne();
			if (found.getQuantity() == 0)
				items.remove(found);
		}
		computeTotal();
	}
	
	/*public int getQuantity(){
		int sum = 0;
		Iterator<OrderItem> it = items.iterator();
		while(it.hasNext()){
			sum += ((OrderItem)it.next()).getQuantity();
		}
		return sum;
	}*/


}
