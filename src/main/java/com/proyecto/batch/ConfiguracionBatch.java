package com.proyecto.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.proyecto.batch.listener.JobEnvioCorreoListener;
import com.proyecto.batch.processor.EnvioCorreoItemProcessor;
import com.proyecto.dto.EnvioCorreoDTO;


@Configuration
@EnableBatchProcessing
public class ConfiguracionBatch 
{
	
	@Autowired public JobBuilderFactory jobBuilderFactory;
	@Autowired public StepBuilderFactory stepBuilderFactory;
	@Autowired public DataSource dataSource;
	
	// Este es el job

	@Bean
	public Job importPersonaJob(JobEnvioCorreoListener listener)   // Se coloca como argumento una llamada a   com.proyecto.batch.listener.JobEnvioCorreoListener
	{
		return jobBuilderFactory.get("importPersonaJob")
			.incrementer(new RunIdIncrementer())  		// Si tenemos varios batch, cada uno de ellos tiene un id de ejecucion diferente, por eso se coloco el incrementer
			.listener(listener)                         // Se le indica al job cual es el listener que se ejecutara en caso de que el estado del job cambie
			.flow(step1()).end().build();        		

	}
	
	// Este es un step
	// Un batch se compone de uno o muchos step. Cada step se compone de un item reader (Obligatorio), un item writer(Obligatorio) y un processor (Opcional)
	
	 @Bean
	 public Step step1() 
	 {
		 return stepBuilderFactory.get("step1").<EnvioCorreoDTO, EnvioCorreoDTO> chunk(10)
		    .reader(reader())
		    .processor(processor())
		    .writer(writer())
		    .build();
	 }
		
	 // Este es un item reader. Un item reader es el encargado de obtener la informacion. 
	 // Esta informacion puede venir de un archivo, de una base de datos o de una cola de mensajes

	 @Bean
	 public JdbcCursorItemReader<EnvioCorreoDTO> reader()
	 {

		 String sql = "SELECT " + 
			"a.id idEnvioCorreoContactoPersona, " +
			"a.contacto_persona_id, a.estado, b.email, c.asunto_correo, c.contenido_correo " + 
		 "FROM " + 
			"envio_correo_contacto_persona a " +  
		    "inner join contacto_persona b on a.contacto_persona_id = b.id " + 
		    "inner join envio_masivo_correo c on a.envio_masivo_correo_id = c.id " +
		 "WHERE " +
		 	"c.estado = 'en cola' AND " +
		 	"a.estado <> 'envio fallido'";

		 JdbcCursorItemReader<EnvioCorreoDTO> reader = new JdbcCursorItemReader<EnvioCorreoDTO>();
		 reader.setDataSource(dataSource);
		 reader.setSql(sql);   
		 reader.setRowMapper(new LlenarMapRow());
	  
		 return reader;
	 }
	 
	 public class LlenarMapRow implements RowMapper<EnvioCorreoDTO>
	 {
		  @Override
		  public EnvioCorreoDTO mapRow(ResultSet rs, int rowNum) throws SQLException 
		  {
			  EnvioCorreoDTO c = new EnvioCorreoDTO();
			  
			  c.setIdEnvioCorreoContactoPersona(rs.getInt("idEnvioCorreoContactoPersona"));
			  c.setIdContactoPersona(rs.getInt("contacto_persona_id"));
			  c.setEstado(rs.getString("estado"));
			  c.setEmail(rs.getString("email"));
			  c.setAsuntoCorreo(rs.getString("asunto_correo"));
			  c.setContenidoCorreo(rs.getString("contenido_correo"));

			  return c;
		  }
	 }
	
	
	 // Este es el writer. Tambien podria ser de tipo FileWriter. Si existe un item reader, entonces debe existir al menos un item writer
	 @Bean
	 public JdbcBatchItemWriter<EnvioCorreoDTO> writer() 
	 {
		 JdbcBatchItemWriter<EnvioCorreoDTO> writer = new JdbcBatchItemWriter<EnvioCorreoDTO>();
		
		 writer.setDataSource(dataSource);
		 writer.setSql("UPDATE envio_correo_contacto_persona SET estado = ? WHERE id = ? AND estado = 'sin enviar'");

		 ItemPreparedStatementSetter<EnvioCorreoDTO> sentenciaPreparada = (objeto, ps) -> 
		 {
			 ps.setString(1, objeto.getEstado());  // Dependiendo del envio de email realizado por el EnvioCorreoItemProcessor, el estado sera:  enviado  o  envio fallido
			 ps.setInt(2, objeto.getIdEnvioCorreoContactoPersona());
		 };

		 writer.setItemPreparedStatementSetter(sentenciaPreparada);
		 return writer;
	 } 
	
	
	 // Este es el processor
	 @Bean
	 public EnvioCorreoItemProcessor processor()  // Esto llama a la clase EnvioCorreoItemProcessor
	 {   
		 return new EnvioCorreoItemProcessor();
	 }
	
	
}































































