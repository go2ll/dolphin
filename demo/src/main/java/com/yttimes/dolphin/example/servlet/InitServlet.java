package com.yttimes.dolphin.example.servlet;

import com.yttimes.dolphin.UploadXmlService;
import com.yttimes.dolphin.support.annotation.DolphinJTAProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitServlet extends HttpServlet {

  private static final long serialVersionUID = -2483327089714497970L;
  private Logger logger = LoggerFactory.getLogger(InitServlet.class);

  @Override
  public void init(ServletConfig config) throws ServletException {
    try {
      logger.info("start upload process definition..");
      UploadXmlService service = DolphinJTAProxy.getProxy(UploadXmlService.class);
      service.upload();
      logger.info("upload successfully!");
    } catch (Exception ex) {
      logger.error("error:", ex);
      throw new ServletException(ex);
    }
  }

  @Override
  public void destroy() {
    logger = null;
  }

}
