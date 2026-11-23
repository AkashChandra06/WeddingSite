package com.akashpriya.wedding.service;

import com.akashpriya.wedding.model.FamilyMember;

import java.util.List;

public interface FamilyService {

    /** Returns the full flat list of family members for a given side. */
    List<FamilyMember> getFlatBySide(String side);

    /** Returns the root nodes (generation 0) of a side, each with nested children populated recursively. */
    List<FamilyMember> getFamilyTree(String side);

    /** Returns a simple flat list (for the "Family Introduction" cards) - typically parents & couple generation. */
    List<FamilyMember> getIntroductionMembers(String side);
}

