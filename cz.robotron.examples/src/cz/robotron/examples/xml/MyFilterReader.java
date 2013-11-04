package cz.robotron.examples.xml;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class MyFilterReader extends FilterReader {

    protected MyFilterReader(Reader in) {
        super(in);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int read() throws IOException {
        int read;
        do {
            read = super.read();
        } while (read == '@');

        return read;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = super.read(cbuf, off, len);

        if (read == -1) {
            return -1;
        }

        int pos = off - 1;
        for (int readPos = off; readPos < off + read; readPos++) {
            char c = cbuf[readPos];
            if (c != '\r' && c != '\n' && c != '\t') {
                pos++;
                cbuf[pos] = cbuf[readPos];
            }

        }
        return pos - off + 1;
    }
}
