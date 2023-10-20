package com.gt.tablewriter;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

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
  public void prepare() {
    if (template == null) {
      template = properties.getProperty("TEMPLATE", null);
    }
    if (template == null) {
      template = loadTemplateFromResources();
    }
    if (this.template.startsWith("<!DOCTYPE html>")) {
      this.template = this.template.substring(15);
    }
    super.prepare();
  }

  @Override
  public void writeTo(OutputStream outputStream) throws IOException {
    this.doc.outerHtml();

    PdfRendererBuilder render = new PdfRendererBuilder();

    render.withHtmlContent(this.doc.outerHtml(), null);
    render.toStream(outputStream);
    render.run();
  }
}
