<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Attribute used for table border -->
	<xsl:attribute-set name="tableBorder">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:template match="items">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="22.0cm" margin="1cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="36pt" font-family="Times New Roman"
						color="black" space-after="5mm" text-align="center" font-weight="bold">
						Order Invoice
					</fo:block>
					<fo:block font-size="16pt" font-family="Times New Roman"
						color="black" space-after="5mm" text-align="center" font-weight="bold">
					</fo:block>
					<fo:block font-size="16pt" font-family="Times New Roman"
						color="black" space-after="5mm" text-align="center" font-weight="bold">
					</fo:block>
					<fo:block font-size="16pt" font-family="Times New Roman"
						color="black" space-after="5mm" text-align-last="justify" font-weight="bold">
						Order Id:
						<xsl:value-of select="order_id" />
						<fo:leader leader-pattern="space"/>
						Date and Time:
						<xsl:value-of select="datetime" />
					</fo:block>
					<fo:block font-size="12pt" space-before="30pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate" line-height="25px" text-align="center">
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-header background-color="#f5f5f5">
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" font-family="Times New Roman">NAME</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" font-family="Times New Roman">MRP</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" font-family="Times New Roman">QUANTITY</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-weight="bold" font-family="Times New Roman">PRICE</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body end-indent="0in">
								<xsl:apply-templates select="item" />
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="15pt" font-family="Times New Roman"
						color="black" font-weight="bold" space-after="5mm" space-before="15pt" text-align="right">
						Total: Rs.
						<xsl:value-of select="total" />
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	 <fo:table-row font-weight="bold">
            <fo:table-cell number-columns-spanned="3" text-align="right" padding-right="3pt">
              <fo:block>Total</fo:block>
            </fo:table-cell>
            <fo:table-cell  text-align="right" padding-right="3pt" background-color="#f5f5f5" border="1px solid #b8b6b6" >
              <fo:block>
                <xsl:value-of select="total" />
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
	<xsl:template match="item">
		<fo:table-row >
			<fo:table-cell xsl:use-attribute-sets="tableBorder" font-family="Times New Roman">
				<fo:block>
					<xsl:value-of select="name" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder" font-family="Times New Roman">
				<fo:block>
					<xsl:value-of select="mrp" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder" font-family="Times New Roman">
				<fo:block>
					<xsl:value-of select="quantity" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="tableBorder" font-family="Times New Roman">
				<fo:block>
					<xsl:value-of select="Price" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>