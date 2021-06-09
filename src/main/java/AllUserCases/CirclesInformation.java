package AllUserCases;

public class CirclesInformation {

    public String filter;
    public String terms;


    public CirclesInformation(String filter, String terms) {
        this.filter = filter;
        this.terms = terms;
    }

    public String getFilter() {
        return filter;
    }

    public String getTerms() {
        return terms;
    }
}
