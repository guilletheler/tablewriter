package com.gt.tablewriter;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;
import org.jsoup.helper.W3CDom;

/**
 * Genera un HTML y luego lo transforma a PDF
 */
public class PdfTableWriter extends HtmlTableWriter {

  public static final String PROPERTY_PAGE_WIDTH = "PAGE_WIDTH";
  public static final String PROPERTY_PAGE_HEIGHT = "PAGE_HEIGHT";

  public PdfTableWriter() {
    super();
    this.prepare();
  }

  public PdfTableWriter(Properties properties) {
    super(properties);
    this.prepare();
  }

  @Override
  public void writeTo(OutputStream outputStream) throws IOException {
    this.doc.outerHtml();

    PdfRendererBuilder pdfBuilder = new PdfRendererBuilder();

    var width = Optional.ofNullable(getProperties().getProperty(PROPERTY_PAGE_WIDTH))
        .map(w -> Integer.valueOf(w)).orElse(210);

    var height = Optional.ofNullable(getProperties().getProperty(PROPERTY_PAGE_HEIGHT))
        .map(w -> Integer.valueOf(w)).orElse(297);

    pdfBuilder.useDefaultPageSize(width, height, PdfRendererBuilder.PageSizeUnits.MM);
    // pdfBuilder.usePdfAConformance(PdfRendererBuilder.PdfAConformance.PDFA_3_A);

    pdfBuilder.withW3cDocument(new W3CDom().fromJsoup(this.doc), "/");
    pdfBuilder.toStream(outputStream);
    pdfBuilder.run();
  }
}
