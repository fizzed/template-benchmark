package com.mitchellbosecke.benchmark;

import java.io.*;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.samskivert.mustache.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import com.mitchellbosecke.benchmark.model.Stock;

public class Mustache_Samskivert extends BaseBenchmark {

    private Template template;

    @Setup
    public void setup() {
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("templates/stocks.mustache.html");
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        template = com.samskivert.mustache.Mustache.compiler().withEscaper(Escapers.NONE).compile(reader);
    }

    @SuppressWarnings("unchecked")
    @Benchmark
    public String benchmark() {

        Map<String, Object> data = getContext();
        data.put("items", new StockCollection((Collection<Stock>) data.get("items")));

        Writer writer = new StringWriter();
        template.execute(data, writer);
        return writer.toString();
    }

    /**
     * This is a modified copy of
     * {@link com.github.mustachejava.util.DecoratedCollection} - we need the
     * first element at index 1.
     *
     * @param <T>
     */
    private class StockCollection extends AbstractCollection<StockView> {

        private final Collection<Stock> c;

        public StockCollection(Collection<Stock> c) {
            this.c = c;
        }

        @Override
        public Iterator<StockView> iterator() {
            final Iterator<Stock> iterator = c.iterator();
            return new Iterator<StockView>() {

                int index = 1;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public StockView next() {
                    Stock next = iterator.next();
                    int current = index++;
                    return new StockView(current, current == 1, !iterator.hasNext(), next);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int size() {
            return c.size();
        }
    }

    class StockView {

        public final int index;

        public final boolean first;

        public final boolean last;

        public final Stock value;

        public final String negativeClass;

        public final String rowClass;

        public StockView(int index, boolean first, boolean last, Stock value) {
            this.index = index;
            this.first = first;
            this.last = last;
            this.value = value;
            this.negativeClass = value.getChange() > 0 ? "" : "class=\"minus\"";
            this.rowClass = index % 2 == 0 ? "even" : "odd";
        }
    }

}
