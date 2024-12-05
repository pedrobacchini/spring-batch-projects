package com.springbatch.simplepartitionerlocal.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;

public class CustomArquivoReader<T> implements ItemStreamReader<T> {

    private final FlatFileItemReader<T> delegate;
    private int limit;

    public CustomArquivoReader(final FlatFileItemReader<T> delegate, final int limit) {
        this.delegate = delegate;
        this.limit = limit;
    }

    @Override
    public T read() throws Exception {
        if (limit > 0) {
            limit--;
            return delegate.read();
        } else return null;
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

}
