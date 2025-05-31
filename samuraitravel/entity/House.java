package com.example.samuraitravel.entity;
//モデル：エンティティ
//クラスHouseとSQLテーブルのhousesをmappingしている
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity	//クラスをデーターベースのテーブルと対応付ける
@Table(name = "houses")
@Data //このアノテーションをクラスにつけば、ゲッターとセッターが自動生成される。
public class House {
    @Id //エンティティのフィールドに@Idアノテーションをつけることで、
        //そのフィールドを主キーに指定できる
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    //主キーの値を自動採番します。IDENTITY を指定すると、
    //データベースの AUTO_INCREMENT 機能を利用して主キーの値を自動で増やす

    @Column(name = "name")
    private String name;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}