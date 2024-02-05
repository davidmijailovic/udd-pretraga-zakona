package com.example.ddmdemo.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "document_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class DocumentIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "title")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    @Field(type = FieldType.Integer, store = true, name = "database_id")
    private Integer databaseId;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @MultiField(
            mainField = @Field(type = FieldType.Text, store = true, name = "surname", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256)})
    private String surname;

    @MultiField(
            mainField = @Field(type = FieldType.Text, store = true, name = "government_name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256)})
    private String governmentName;

    @Field(type = FieldType.Text, store = true, name = "government_type", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String governmentType;

    @Field(type = FieldType.Text, store = true, name = "government_address", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String governmentAddress;

    @GeoPointField
    @MultiField(
            mainField = @Field(type = FieldType.Double, store = true, name = "location"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256)})
    private GeoPoint location;

}
