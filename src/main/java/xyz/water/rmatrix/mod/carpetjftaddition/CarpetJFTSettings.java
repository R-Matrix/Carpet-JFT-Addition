package xyz.water.rmatrix.mod.carpetjftaddition;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public class CarpetJFTSettings {


//========================================
//
//              以下为验证器
//
//========================================

    private static class jft_intValidator extends Validator<Integer> {
        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return newValue;
        }
    }

    private static class jft_From0To1_Or_Neg1 extends Validator<Double>
    {
        @Override
        public Double validate(ServerCommandSource source, CarpetRule<Double> currentRule, Double newValue, String typedString)
        {
            if(newValue == -1 || (newValue >= 0 && newValue <= 1)){
                return newValue;
            }
            return null;
        }
    }



//========================================
//
//              以下为rules
//
//========================================



    @Rule(
            options = {"VANILLA", "RAINING", "ANY", "DISABLED"},
            categories = {SURVIVAL, "JFT"}
    )
    public static String channelingWeather = "VANILLA";


    @Rule(categories = {SURVIVAL, "JFT"})
    public static Boolean channelingCanSeeSky = true;


    @Rule(
            options = {"VANILLA", "ANY", "WATERRorLAVA", "DISABLED"},
            categories = {SURVIVAL, "JFT"}
    )
    public static String riptideTouchingWater = "VANILLA";


    @Rule(
            options = {"10", "5", "0"},
            strict = false,
            validators = {Validators.NonNegativeNumber.class, jft_intValidator.class},
            categories = {SURVIVAL, "JFT"}
    )
    public static int changeUseTridentTimeTicks = 10;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean drownedReinforcementCanSpawnZombie = false;


    @Rule(  options = {"-1.0", "0", "0.5", "1.0"},
            strict = false,
            validators = {jft_From0To1_Or_Neg1.class},
            categories = {SURVIVAL, "JFT"}
    )
    public static double drownedSpawnHasEquipments = -1.0;


    @Rule(  options = {"-1.0", "0", "0.5", "1.0"},
            strict = false,
            validators = {jft_From0To1_Or_Neg1.class},
            categories = {SURVIVAL, "JFT"}
    )
    public static double reinforcementAttributeInit = -1.0;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean canPlayerPickUpLikeArrows = false;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean tridentMultipleDamage = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean impalingWaterContact = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean turtleKelpFeedingAndViviparousBreeding = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean frogDyeFeedingAndViviparousBreeding = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean allowAmethystBudCanGrowAtWater = true;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean amethystPistonBehaviorNormal = false;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean glowLichenCanShadowBlocks = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean signTextGlowingAlways = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean wetSpongeDriesOnDesertAndBedLands = false;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean goatHayFeedingAndChildScreaming = false;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean turtleEggsDriedKelpBlockFaster = false;


    @Rule(options = {"10", "20", "40"},
            strict = false,
            validators = {jft_intValidator.class},
            categories = {SURVIVAL, "JFT"})
    public static int animalsSpanLimit = 10;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean animalsRaleSetting = true;


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean canTillFarmlandBelowBlock = false;


    @Rule(categories = {SURVIVAL, FEATURE, "JFT"})
    public static boolean shulkerBaseTickIfOnTwistingVine = false;


    @Rule(options = {"false", "true", "allowEmptyHand"},
            categories = {SURVIVAL, "JFT"})
    public static String interceptItemFrameDrop = "false";


    @Rule(categories = {SURVIVAL, "JFT"})
    public static boolean canArrowDamageItemFrame = false;


    @Rule(categories = {CREATIVE, "JFT", FEATURE})
    public static boolean spawnEggYellowGlassSmallVariant = false;


    @Rule(categories = {CREATIVE, "JFT", FEATURE})
    public static boolean spawnEggGreenGlassLargeVariant = false;


    @Rule(options = {"1", "5", "10"},
            strict = false,
            validators = {Validators.NonNegativeNumber.class, jft_intValidator.class},
            categories = {SURVIVAL, "JFT", FEATURE})
    public static int trialSpawnerRecognitionSLPlayerAsMul = 1;


    @Rule(categories = {SURVIVAL, "JFT", FEATURE})
    public static boolean jftMapSyncProtocol = false;
}
