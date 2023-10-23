package com.gt.tablewriter;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.jsoup.helper.W3CDom;

public class PdfTableWriter extends HtmlTableWriter {

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

    PdfRendererBuilder render = new PdfRendererBuilder();

    render.withW3cDocument(new W3CDom().fromJsoup(this.doc), "/");
    render.toStream(outputStream);
    render.run();
  }
}
