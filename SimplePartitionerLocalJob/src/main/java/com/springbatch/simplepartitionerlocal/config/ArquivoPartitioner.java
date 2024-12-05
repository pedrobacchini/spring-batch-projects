package com.springbatch.simplepartitionerlocal.config;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ArquivoPartitioner implements Partitioner {

    @Value("${migracaoDados.totalRegistros}")
    public Integer totalRegistros;

    @Value("${migracaoDados.gridSize}")
    public Integer gridSize;

    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        final Map<String, ExecutionContext> map = new HashMap<>();
        for (int i = 0; i < gridSize; i++) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putInt("particao", i);
            map.put("partition" + i, executionContext);
        }
        return map;
    }

    public int calcularPrimeiroItemLeitura(Integer particao) {
        return particao * (totalRegistros / gridSize);
    }

    public int getItemsLimit() {
        return totalRegistros / gridSize;
    }

}
