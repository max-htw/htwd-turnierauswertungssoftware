import java.util.*;

class PDF {

    private List<PDFObject> pdfObjects = new ArrayList<>();

    public PDF(PDFObject root, PDFObject... objects) {
        pdfObjects.add(root);
        pdfObjects.addAll(Arrays.asList(objects));
    }

    public String build() {
        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.1\n\n");

        for (PDFObject pdfObject : pdfObjects) {
            pdf.append(pdfObject.build());
        }

        pdf.append("trailer\n  << /Root " + pdfObjects.get(0).getObjectReference() + "\n   /Size "
                + (pdfObjects.size() + 1) + "\n  >>\n" + "%%EOF");

        return pdf.toString();
    }

}