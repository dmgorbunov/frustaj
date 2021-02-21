package com.dmgorbunov.frustaj.data;

import java.util.Arrays;

public enum FLPEventType {

    UNKNOWN(-1),

    ENABLED(0),
    NOTE_ON(1),
    VOL(2),
    PAN(3),
    MIDI_CHAN(4),
    MIDI_NOTE(5),
    MIDI_PATCH(6),
    MIDI_BANK(7),
    LOOP_ACTIVE(9),
    SHOW_INFO(10),
    SHUFFLE(11),
    MAIN_VOLUME(12),
    STRETCH(13),
    PITCHABLE(14),
    ZIPPED(15),
    DELAY_FLAGS(16),
    PATTERN_LENGTH(17),
    BLOCK_LENGTH(18),
    USE_LOOP_POINTS(19),
    LOOP_TYPE(20),
    CHANNEL_TYPE(21),
    MIX_SLICE_NUM(22),
    PAN_VOL_TAB(23),
    EFFECTCHANNELMUTED(27),
    PROJECT_REG_VERSION(28),
    PROJECT_APDC(29),
    PROJECT_TRUNCATE_CLIPNOTES(30),
    PROJECT_EEAUTOMODE(31),

    NEWCHAN(FLPEventType.WORD),
    NEWPAT( FLPEventType.WORD + 1),
    TEMPO(FLPEventType.WORD + 2),
    CURRENT_PATTERN_NUMBER(FLPEventType.WORD + 3),
    PATDATA(FLPEventType.WORD + 4),

    FADE_STEREO(FLPEventType.WORD + 6), // FX Flags?

    FX(FLPEventType.WORD + 5),
    FX3(FLPEventType.WORD + 22),
    CUTOFF(FLPEventType.WORD + 7),
    RESONANCE(FLPEventType.WORD + 19),

    DOTVOL(FLPEventType.WORD + 8),
    DOTPAN(FLPEventType.WORD + 9),
    PREAMP(FLPEventType.WORD + 10),
    DECAY(FLPEventType.WORD + 11),
    ATTACK(FLPEventType.WORD + 12),
    DOTNOTE(FLPEventType.WORD + 13),
    DOTPITCH(FLPEventType.WORD + 14),
    DOTMIX(FLPEventType.WORD + 15),
    MAINPITCH(FLPEventType.WORD + 16),
    RANDCHAN(FLPEventType.WORD + 17),
    MIXCHAN(FLPEventType.WORD + 18),

    LOOPBAR(FLPEventType.WORD + 20),
    STDEL(FLPEventType.WORD + 21),

    DOTRESO(FLPEventType.WORD + 23),
    DOTCUTOFF(FLPEventType.WORD + 24),
    SHIFTDELAY(FLPEventType.WORD + 25),
    LOOPENDBAR(FLPEventType.WORD + 26),
    DOT(FLPEventType.WORD + 27),
    DOTSHIFT(FLPEventType.WORD + 28),
    LAYERCHANS(FLPEventType.WORD + 30),
    DOT_REL(FLPEventType.WORD + 32),
    SWING_MIX(FLPEventType.WORD + 33),

    PLAYLISTITEM(FLPEventType.INT + 1),
    ECHO(FLPEventType.INT + 2),
    FXSINE(FLPEventType.INT + 3),
    CUTCUTBY(FLPEventType.INT + 4),
    WINDOWH(FLPEventType.INT + 5),
    MIDDLENOTE(FLPEventType.INT + 7),
    RESERVED(FLPEventType.INT + 8),
    MAINRESOCUTOFF(FLPEventType.INT + 9),
    DELAYRESO(FLPEventType.INT + 10),
    REVERB(FLPEventType.INT + 11),
    INTSTRETCH(FLPEventType.INT + 12),
    SSNOTE(FLPEventType.INT + 13),
    FINETUNE(FLPEventType.INT + 14),
    SAMPLE_FLAGS(FLPEventType.INT + 15),
    LAYER_FLAGS(FLPEventType.INT + 16),
    CHAN_FILTER_NUM(FLPEventType.INT + 17),
    CURRENT_FILTER_NUM(FLPEventType.INT + 18),
    FX_OUT_CHAN_NUM(FLPEventType.INT + 19),
    NEW_TIME_MARKER(FLPEventType.INT + 20),
    FX_COLOR(FLPEventType.INT + 21),
    PATTERN_COLOR(FLPEventType.INT + 22),
    PATTERN_AUTO_MODE(FLPEventType.INT + 23), // obsolete
    SONG_LOOP_POSITION(FLPEventType.INT + 24),
    AU_SMP_RATE(FLPEventType.INT +25),
    FX_IN_CHANNEL_NUM(FLPEventType.INT + 26),

    FINE_TEMPO(FLPEventType.INT + 28),

    TEXT_CHANNAME(FLPEventType.TEXT),
    TEXT_PATNAME(FLPEventType.TEXT + 1),
    TEXT_TITLE(FLPEventType.TEXT + 2),
    TEXT_COMMENT(FLPEventType.TEXT + 3),
    TEXT_SAMPLEFILENAME(FLPEventType.TEXT + 4),
    TEXT_URL(FLPEventType.TEXT + 5),
    TEXT_COMMENT_RTF(FLPEventType.TEXT + 6),
    TEXT_VERSION(FLPEventType.TEXT + 7),

    TEXT_DATAPATH(FLPEventType.TEXT + 10),
    TEXT_EFFECTCHANNAME(FLPEventType.TEXT + 12),
    TEXT_GENRE(FLPEventType.TEXT + 14),
    TEXT_AUTHOR(FLPEventType.TEXT + 15),
    TEXT_MIDICTRLS(FLPEventType.TEXT + 16),
    TEXT_DELAY(FLPEventType.TEXT + 17),
    TEXT_TS404PARAMS(FLPEventType.TEXT + 18),
    TEXT_DELAYLINE(FLPEventType.TEXT + 19),

    // Plug-ins
    TEXT_PLUGIN(FLPEventType.TEXT + 20),
    COLOR(FLPEventType.INT),
    TEXT_PLUGIN_NAME_DEFAULT(FLPEventType.TEXT + 9),
    TEXT_PLUGIN_NAME(FLPEventType.TEXT + 11),
    PLUGIN_ICON(FLPEventType.INT + 27),

    TEXT_PLUGINPARAMS(FLPEventType.TEXT + 21),
    TEXT_CHANPARAMS(FLPEventType.TEXT + 23),
    TEXT_ENVLFOPARAMS(FLPEventType.TEXT + 26),
    TEXT_BASICCHANPARAMS(FLPEventType.TEXT + 27),
    TEXT_OLDFILTERPARAMS(FLPEventType.TEXT + 28),
    TEXT_AUTOMATIONDATA(FLPEventType.TEXT + 31),
    TEXT_PATTERNNOTES(FLPEventType.TEXT + 32),
    TEXT_CHANGROUPNAME(FLPEventType.TEXT + 39),
    TEXT_PLAYLISTITEMS(FLPEventType.TEXT + 41), // 233
    TEXT_PROJECT_TIME(FLPEventType.TEXT + 45);

    private final static int BYTE = 0;
    private final static int WORD = 64;
    private final static int INT = 128;
    private final static int TEXT = 192;

    private final long value;
    private final SubClass subClass;

    FLPEventType(int value) {
        this.value = value;
        if (value <= 63) this.subClass = SubClass.BYTE;
        else if (value <= 127) this.subClass = SubClass.WORD;
        else if (value <= 191) this.subClass = SubClass.INT;
        else this.subClass = SubClass.TEXT;
    }

    public long getValue() {
        return value;
    }

    public SubClass getSubClass() {
        return subClass;
    }

    public boolean isSkippable() {
        return
                this == ENABLED
                        || this == UNKNOWN
                        || this == FX_IN_CHANNEL_NUM
                        || this == FX_OUT_CHAN_NUM;
    }

    public enum SubClass {
        BYTE,
        WORD,
        INT,
        TEXT;
    }

    public static FLPEventType find(long id) {
        return Arrays.stream(values()).filter(v -> v.value == id).findAny().orElse(UNKNOWN);
    }

}

