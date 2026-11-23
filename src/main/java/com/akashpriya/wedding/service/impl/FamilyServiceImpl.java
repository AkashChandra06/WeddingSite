package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.constants.AppConstants;
import com.akashpriya.wedding.model.FamilyMember;
import com.akashpriya.wedding.service.FamilyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds a deep, multi-generation family tree for both sides:
 * Generation 0 = Grandparents
 * Generation 1 = Parents
 * Generation 2 = Couple / Siblings
 * Generation 3 = Children / Cousins (next generation, optional / future use)
 */
@Service
public class FamilyServiceImpl implements FamilyService {

    private final List<FamilyMember> allMembers = buildSampleData();

    private List<FamilyMember> buildSampleData() {
        List<FamilyMember> members = new ArrayList<>();

        // ---------- Akash's side ----------
        // Generation 1: Parents (root node is Nagesh Kumar; Poonam is his spouse, rendered beside him)
        members.add(new FamilyMember(1L, "Nagesh Kumar", "Father of the Groom", AppConstants.SIDE_AKASH, 1,
                "/images/family/akash-father.jpg", "A dedicated and loving father, pillar of the family.", null));
        members.add(new FamilyMember(2L, "Poonam", "Mother of the Groom", AppConstants.SIDE_AKASH, 1,
                "/images/family/akash-mother.jpg", "The heart of the family, known for her warmth.", null, 1L));

        // Generation 2: Elder brother & groom (children of Nagesh Kumar, id 1) - Anurag first so
        // Akash renders to his right.
        members.add(new FamilyMember(4L, "Anurag", "Elder Brother of the Groom", AppConstants.SIDE_AKASH, 2,
                "/images/family/akash-brother.jpg", "Akash's elder brother, a caring and supportive presence.", 1L));
        members.add(new FamilyMember(3L, "Akash", "Groom", AppConstants.SIDE_AKASH, 2,
                "/images/couple/akash.jpg", "Software engineer with a passion for travel and music.", 1L));

        // Anurag's wife (rendered beside him) and their son (child of Anurag, id 4)
        members.add(new FamilyMember(5L, "Nikita", "Bhabhi (Anurag's Wife)", AppConstants.SIDE_AKASH, 2,
                "/images/family/akash-bhabhi.jpg", "Anurag's wife, a warm and graceful addition to the family.", null, 4L));
        members.add(new FamilyMember(6L, "Bhavyansh (Anik)", "Nephew (Anurag & Nikita's Son)", AppConstants.SIDE_AKASH, 3,
                "/images/family/akash-nephew.jpg", "The little bundle of joy in the family.", 4L));

        // ---------- Priya's side ----------
        // Generation 1: Parents (root node is Sunil Kumar; Aarti Devi is his spouse, rendered beside him)
        members.add(new FamilyMember(7L, "Sunil Kumar", "Father of the Bride", AppConstants.SIDE_PRIYA, 1,
                "/images/family/priya-father.jpg", "A gentle and wise father figure.", null));
        members.add(new FamilyMember(8L, "Aarti Devi", "Mother of the Bride", AppConstants.SIDE_PRIYA, 1,
                "/images/family/priya-mother.jpg", "A loving homemaker and the heart of the family.", null, 7L));

        // Generation 2: Bride & younger siblings (children of Sunil Kumar, id 7)
        members.add(new FamilyMember(9L, "Priya", "Bride", AppConstants.SIDE_PRIYA, 2,
                "/images/couple/priya.jpg", "Architect who loves painting and classical dance.", 7L));
        members.add(new FamilyMember(10L, "Aman", "Younger Brother of the Bride", AppConstants.SIDE_PRIYA, 2,
                "/images/family/priya-brother.jpg", "Priya's younger brother, full of energy and enthusiasm.", 7L));
        members.add(new FamilyMember(11L, "Riya", "Younger Sister of the Bride", AppConstants.SIDE_PRIYA, 2,
                "/images/family/priya-sister.jpg", "Priya's younger sister, sweet and cheerful.", 7L));

        return members;
    }

    @Override
    public List<FamilyMember> getFlatBySide(String side) {
        return allMembers.stream()
                .filter(m -> m.getSide().equalsIgnoreCase(side))
                .collect(Collectors.toList());
    }

    @Override
    public List<FamilyMember> getFamilyTree(String side) {
        List<FamilyMember> sideMembers = getFlatBySide(side);

        // Members who married into the family (spouseId set) are not rendered as independent
        // roots/children - instead they are attached to their blood-relative partner as `.spouse`
        // so the couple can be rendered side by side in the tree.
        Map<Long, FamilyMember> spouseOf = sideMembers.stream()
                .filter(m -> m.getSpouseId() != null)
                .collect(Collectors.toMap(FamilyMember::getSpouseId, m -> m));

        List<FamilyMember> bloodMembers = sideMembers.stream()
                .filter(m -> m.getSpouseId() == null)
                .collect(Collectors.toList());

        Map<Long, List<FamilyMember>> byParent = bloodMembers.stream()
                .filter(m -> m.getParentId() != null)
                .collect(Collectors.groupingBy(FamilyMember::getParentId));

        // Roots are members without a parent (generation 0)
        List<FamilyMember> roots = bloodMembers.stream()
                .filter(m -> m.getParentId() == null)
                .collect(Collectors.toList());

        for (FamilyMember root : roots) {
            attachChildren(root, byParent, spouseOf);
        }
        return roots;
    }

    private void attachChildren(FamilyMember node, Map<Long, List<FamilyMember>> byParent,
                                 Map<Long, FamilyMember> spouseOf) {
        node.setSpouse(spouseOf.get(node.getId()));
        List<FamilyMember> children = byParent.getOrDefault(node.getId(), new ArrayList<>());
        node.setChildren(children);
        for (FamilyMember child : children) {
            attachChildren(child, byParent, spouseOf);
        }
    }

    @Override
    public List<FamilyMember> getIntroductionMembers(String side) {
        // Family introduction section shows immediate family: parents + couple + siblings + their spouse/children
        return getFlatBySide(side).stream()
                .filter(m -> m.getGeneration() >= 1 && m.getGeneration() <= 3)
                .collect(Collectors.toList());
    }
}

