package de.jablab.sebschlicht.series;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import de.jablab.sebschlicht.kits.SeriesSession;

public class SeriesSessionTest {

    private static final String PATH_VALID = "src/test/resources/raw/valid.csv";

    private static final String PATH_EMPTY = "src/test/resources/raw/empty.csv";

    private static final String PATH_NOT_FOUND = "src/test/resources/raw/not-found.csv";

    private SeriesSession session;

    /**
     * Test if valid test series are loaded correctly.
     *
     * @throws IOException
     *             if the test file is missing or malformed
     */
    @Test
    public void testLoadSeries() throws IOException {
        this.loadSessionFromCsv(PATH_VALID);
        assertEquals(3, this.session.getSeries().size());
    }

    /**
     * Test if the empty test file is loaded correctly.
     *
     * @throws IOException
     *             if the test file is missing or malformed
     */
    @Test
    public void testLoadNoSeries() throws IOException {
        this.loadSessionFromCsv(PATH_EMPTY);
        assertEquals(0, this.session.getSeries().size());
    }

    /**
     * Test if the <code>FileNotFoundException</code> is thrown when the
     * specified CSV file is missing.
     *
     * @throws FileNotFoundException
     *             if the test succeeds
     * @throws IOException
     *             if the test file is missing or malformed
     */
    @Test(
            expected = FileNotFoundException.class)
    public void testLoadSeriesCsvNotFound() throws FileNotFoundException, IOException {
        this.loadSessionFromCsv(PATH_NOT_FOUND);
    }

    private void loadSessionFromCsv(String csvPath) throws FileNotFoundException,
            IOException {
        this.session = new SeriesSession(null, new FileInputStream(new File(csvPath)));
    }
}
