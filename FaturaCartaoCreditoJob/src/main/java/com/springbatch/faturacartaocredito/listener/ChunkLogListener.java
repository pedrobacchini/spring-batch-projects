package com.springbatch.faturacartaocredito.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkLogListener extends ChunkListenerSupport {

    @Override
    public void beforeChunk(ChunkContext context) {
        final StepExecution stepExecution = context.getStepContext().getStepExecution();
        System.out.println("ChunkLogListener - beforeChunk:getSummary= " + stepExecution.getSummary());
    }

}
