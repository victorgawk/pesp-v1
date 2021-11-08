package app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.model.Operacao;
import app.model.Usuario;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

	List<Operacao> findByDepartureDateAndReturnDateAndDestinationAndUsuario(
			Date departureDate,
			Date returnDate,
			String destination,
			Usuario usuario);
}
