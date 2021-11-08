package app.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operacao")
public class Operacao {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "departure_date")
	private Date departureDate;
	
	@Column(name = "return_date")
	private Date returnDate;
	
	@Column(name = "destination")
	private String destination;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "cancelled")
	private boolean cancelled;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Operacao)) {
			return false;
		}
		Operacao operacao = (Operacao) o;
		return Objects.equals(this.id, operacao.id)
				&& Objects.equals(this.departureDate, operacao.departureDate)
				&& Objects.equals(this.returnDate, operacao.returnDate)
				&& Objects.equals(this.destination, operacao.destination)
				&& Objects.equals(this.usuario.getId(), operacao.usuario.getId())
				&& Objects.equals(this.cancelled, operacao.cancelled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.departureDate, this.returnDate, this.destination, this.usuario.getId(), this.cancelled);
	}

	@Override
	public String toString() {
		return "Operacao" + "operation_id=" + this.id
				+ ", departureDate=" + this.departureDate 
				+ ", returnDate=" + this.returnDate
				+ ", destination=" + this.destination
				+ ", user_id=" + this.usuario.getId()
				+ ", cancelled =" + this.cancelled
				+ '}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
