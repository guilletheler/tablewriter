package com.gt.tablewriter;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfTableWriter extends HtmlTableWriter {

  public PdfTableWriter() {
    super();
  }

  public PdfTableWriter(Properties properties) {
    super(properties);
  }

  @Override
  public void open() {
    if (this.template.startsWith("<!DOCTYPE html>")) {
      this.template = this.template.substring(15);
    }
    super.open();
  }

  @Override
  public void writeTo(OutputStream outputStream) throws IOException {
    this.doc.outerHtml();

    PdfRendererBuilder render = new PdfRendererBuilder();

    Logger.getLogger(getClass().getName()).log(Level.INFO, doc.outerHtml());

    render.withHtmlContent(this.doc.outerHtml(), null);
    render.toStream(outputStream);
    render.run();
  }
}
