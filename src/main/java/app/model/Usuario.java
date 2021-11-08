package app.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "enabled")
	private boolean enabled;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Usuario)) {
			return false;
		}
		Usuario usuario = (Usuario) o;
		return Objects.equals(this.id, usuario.id) && Objects.equals(this.enabled, usuario.enabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.enabled);
	}

	@Override
	public String toString() {
		return "Usuario{" + "user_id=" + this.id + ", enabled=" + this.enabled + '}';
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
