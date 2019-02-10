package com.github.miachm.SODS.spreadsheet;


public final class Style implements Cloneable {
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private Color fontColor;

    public Style() {

    }

    public Style(boolean bold, boolean italic, boolean underline, Color fontColor) {
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.fontColor = fontColor;
    }

    public boolean isDefault()
    {
        return this.equals(new Style());
    }
    
    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public void setBold(boolean bold)
    {
        this.bold = bold;
    }

    public void setItalic(boolean italic)
    {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Style style = (Style) o;

        if (bold != style.bold) return false;
        return italic == style.italic;
    }

    @Override
    public int hashCode() {
        int result = (bold ? 1 : 0);
        result = 31 * result + (italic ? 1 : 0);
        return result;
    }
}
