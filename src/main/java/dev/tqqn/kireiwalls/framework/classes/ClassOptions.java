package dev.tqqn.kireiwalls.framework.classes;

import lombok.Getter;

import java.util.List;

/**
 * The ClassOptions class encapsulates various options and descriptions for player classes.
 * It includes details such as energy consumption, class type, difficulty, styles, diamond equipment, and skill description.
 */
@Getter
public final class ClassOptions {

    private final ClassDescriptions.ClassEnergy classEnergy;
    private final ClassDescriptions.ClassType classType;
    private final ClassDescriptions.ClassDifficulty classDifficulty;
    private final List<ClassDescriptions.ClassStyle> classStyles;
    private final ClassDescriptions.ClassDiamond classDiamond;
    private final ClassDescriptions.ClassSkillDescription classSkillDescription;

    /**
     * Constructs a ClassOptions object with the specified parameters.
     *
     * @param classEnergy The energy consumption details for the class.
     * @param classType The type of the class.
     * @param classDifficulty The difficulty of the class.
     * @param classStyles The list of styles associated with the class.
     * @param classDiamond The diamond equipment associated with the class.
     * @param classSkillDescription The skill description associated with the class.
     */
    public ClassOptions(ClassDescriptions.ClassEnergy classEnergy, ClassDescriptions.ClassType classType, ClassDescriptions.ClassDifficulty classDifficulty, List<ClassDescriptions.ClassStyle> classStyles, ClassDescriptions.ClassDiamond classDiamond, ClassDescriptions.ClassSkillDescription classSkillDescription) {
        this.classEnergy = classEnergy;
        this.classType = classType;
        this.classDifficulty = classDifficulty;
        this.classStyles = classStyles;
        this.classDiamond = classDiamond;
        this.classSkillDescription = classSkillDescription;
    }
}
