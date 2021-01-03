package com.proyecto.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

// Esto es un Listener. Nos permite conocer el estado de ejecucion de nuestro job
// Esta clase sera inyectada en la configuracion del spring batch


@Component
public class JobEnvioCorreoListener extends JobExecutionListenerSupport
{
	// Esto es para poder escribir cosas en consola
	private static final Logger LOG = LoggerFactory.getLogger(JobEnvioCorreoListener.class);

	// Esto es para cuando el estado del job es que finalizo de forma correcta
	@Override
	public void afterJob(JobExecution jobExecution) 
	{
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) 
		{
			LOG.info("PROCESO DE ENVIO DE EMAILS COMPLETADO");
		}
	}
}
