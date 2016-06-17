package com.jnbonner.familymap.Filter;

/**
 * Created by James on 3/24/2016.
 */
public class FilterListItem {
    String filterTitle;
    String filterDescription;
    boolean filterShown;

    public FilterListItem(String title, String description, boolean shown){
        filterTitle = title;
        filterDescription = description;
        filterShown = shown;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public String getFilterDescription() {
        return filterDescription;
    }

    public void setFilterDescription(String filterDescription) {
        this.filterDescription = filterDescription;
    }

    public boolean isFilterShown() {
        return filterShown;
    }

    public void setFilterShown(boolean filterShown) {
        this.filterShown = filterShown;
    }
}
