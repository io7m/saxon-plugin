<?xml version="1.0" encoding="UTF-8"?>
<xt:stylesheet version="2.0"
  xmlns:t="http://io7m.com/schemas/test"
  xmlns:xt="http://www.w3.org/1999/XSL/Transform">  

  <xt:param name="file_output"/>

  <xt:template match="t:test">
    <xt:message terminate="no"><xt:value-of select="concat('result: ',$file_output)"/></xt:message>
    <xt:result-document
      doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
      doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
      encoding="UTF-8"
      href="{$file_output}"
      indent="yes"
      method="xml">
      <p>Hello</p>
    </xt:result-document>
  </xt:template>

</xt:stylesheet>
