package dev.tqqn.kireiwalls.framework.game.classes;

import lombok.Getter;

import java.util.List;

@Getter
public class ClassOptions {

    private final ClassDescriptions.ClassType classType;
    private final ClassDescriptions.ClassDifficulty classDifficulty;
    private final List<ClassDescriptions.ClassStyle> classStyles;
    private final ClassDescriptions.ClassDiamond classDiamond;
    private final ClassDescriptions.ClassSkillDescription classSkillDescription;

    public ClassOptions(ClassDescriptions.ClassType classType, ClassDescriptions.ClassDifficulty classDifficulty, List<ClassDescriptions.ClassStyle> classStyles, ClassDescriptions.ClassDiamond classDiamond, ClassDescriptions.ClassSkillDescription classSkillDescription) {
        this.classType = classType;
        this.classDifficulty = classDifficulty;
        this.classStyles = classStyles;
        this.classDiamond = classDiamond;
        this.classSkillDescription = classSkillDescription;
    }
}
