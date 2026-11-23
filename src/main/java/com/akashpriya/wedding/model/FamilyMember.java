package com.akashpriya.wedding.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A member of Akash's or Priya's family, supporting deep, multi-generation trees via
 * a self-referencing parentId relationship.
 *
 * generation: 0 = grandparents, 1 = parents, 2 = couple/siblings, 3 = children/cousins, etc.
 * side: AKASH or PRIYA (see {@link com.akashpriya.wedding.constants.AppConstants}).
 */
@Entity
@Table(name = "family_members")
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private String side;
    private int generation;
    private String photo;

    @Column(length = 1000)
    private String description;

    /** Id of the parent node in the tree; null for root nodes (e.g. grandparents). */
    private Long parentId;

    /**
     * Id of the blood-relative family member that this person married into the family with
     * (e.g. Poonam's spouseId points to Nagesh Kumar's id). Only set on the spouse who married
     * in; the blood relative does not need this set. Used purely to render the couple side by
     * side in the family tree - it does not affect the parent/child hierarchy.
     */
    private Long spouseId;

    @Transient
    private List<FamilyMember> children = new ArrayList<>();

    @Transient
    private FamilyMember spouse;

    public FamilyMember() {
    }

    public FamilyMember(Long id, String name, String relationship, String side, int generation,
                         String photo, String description, Long parentId) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.side = side;
        this.generation = generation;
        this.photo = photo;
        this.description = description;
        this.parentId = parentId;
    }

    public FamilyMember(Long id, String name, String relationship, String side, int generation,
                         String photo, String description, Long parentId, Long spouseId) {
        this(id, name, relationship, side, generation, photo, description, parentId);
        this.spouseId = spouseId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(Long spouseId) {
        this.spouseId = spouseId;
    }

    public FamilyMember getSpouse() {
        return spouse;
    }

    public void setSpouse(FamilyMember spouse) {
        this.spouse = spouse;
    }

    public List<FamilyMember> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyMember> children) {
        this.children = children;
    }
}

