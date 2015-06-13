package org.moflon.ide.texteditor.helpers;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.moflon.ide.texteditor.editors.COLORS;
import org.moflon.ide.texteditor.editors.ColorManager;

public class TextAttributeHelper {

	public static TextAttribute createTextAttribute(COLORS color, COLORS bgColor, String fontName, int fontHeight, int style, int lineStyle){
		RGB rgb 		 = ColorManager.getInstance().getRGB(color);
		RGB bgRGBColor	 = ColorManager.getInstance().getRGB(bgColor);
		Color swtColor   = ColorManager.getInstance().getColor(rgb);
		Color swtBgColor = null;
		if(bgRGBColor != null)
		{
			swtBgColor = ColorManager.getInstance().getColor(bgRGBColor);
		}
		
		//Get current font settings
		FontData[] fontData =JFaceResources.getTextFont().getFontData();	

		//Set default settings if not set
		if(fontName == null && fontData.length > 0){
			fontName = fontData[0].getName();
		}
		
		if(fontHeight == 0 && fontData.length > 0){
			fontHeight = fontData[0].getHeight();
		}
		
		//Set new font
		Font font = new Font(null, fontName, fontHeight, style);

		return new TextAttribute(
				swtColor,
				swtBgColor,
				lineStyle,
				font);
	}
		
}
