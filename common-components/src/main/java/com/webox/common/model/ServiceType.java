package com.webox.common.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="serviceType")
@Data @NoArgsConstructor
public class ServiceType {
  private @Id String typeId;
  @Indexed
  private int typeTextId; //0, services. 1,activities, 2, news 
  private String typeName; 
  
  private String keywords;
  private Double rank = new Double(0.0001);
  private String status; // active and inactive

}