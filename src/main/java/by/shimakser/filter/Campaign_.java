package by.shimakser.filter;

import by.shimakser.model.Campaign;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Campaign_.class)
public abstract class Campaign_ {

    public static volatile SingularAttribute<Campaign, String> id;
    public static volatile SingularAttribute<Campaign, String> campaignTitle;
    public static volatile SingularAttribute<Campaign, String> campaignDescription;
    public static volatile SingularAttribute<Campaign, String> image;
    public static volatile SingularAttribute<Campaign, String> countries;
    public static volatile SingularAttribute<Campaign, String> languages;
    public static volatile SingularAttribute<Campaign, String> age;
    public static volatile SingularAttribute<Campaign, String> geolocation;
    public static volatile SingularAttribute<Campaign, String> campaignCreatedDate;
    public static volatile SingularAttribute<Campaign, String> campaignDeletedDate;

    public static final String ID = "id";
    public static final String TITLE = "campaignTitle";
    public static final String DESCRIPTION = "campaignDescription";
    public static final String IMAGE = "image";
    public static final String COUNTRIES = "countries";
    public static final String LANGUAGES = "languages";
    public static final String AGE = "age";
    public static final String GEOLOCATION = "geolocation";
    public static final String CREATED_DATE = "campaignCreatedDate";
    public static final String DELETED_DATE = "campaignDeletedDate";
}
