package com.dmgorbunov.frustaj.data;

import java.util.Arrays;
import java.util.List;

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

    NEWCHAN(64),
    NEWPAT(65),
    TEMPO(66),
    CURRENT_PATTERN_NUMBER(67),
    PATDATA(68),

    FADE_STEREO(70),

    FX(69),
    FX3(86),
    CUTOFF(71),
    RESONANCE(83),

    DOTVOL(72),
    DOTPAN(73),
    PREAMP(74),
    DECAY(75),
    ATTACK(76),
    DOTNOTE(77),
    DOTPITCH(78),
    DOTMIX(79),
    MAINPITCH(80),
    RANDCHAN(81),
    MIXCHAN(82),

    LOOPBAR(84),
    STDEL(85),

    DOTRESO(87),
    DOTCUTOFF(88),
    SHIFTDELAY(89),
    LOOPENDBAR(90),
    DOT(91),
    DOTSHIFT(92),
    LAYERCHANS(94),
    DOT_REL(96),
    SWING_MIX(97),

    PLAYLISTITEM(129),
    ECHO(130),
    FXSINE(131),
    CUTCUTBY(132),
    WINDOWH(133),
    MIDDLENOTE(135),
    RESERVED(136),
    MAINRESOCUTOFF(137),
    DELAYRESO(138),
    REVERB(139),
    INTSTRETCH(140),
    SSNOTE(141),
    FINETUNE(142),
    SAMPLE_FLAGS(143),
    LAYER_FLAGS(144),
    CHAN_FILTER_NUM(145),
    CURRENT_FILTER_NUM(146),
    FX_OUT_CHANNEL_NUM(147),
    NEW_TIME_MARKER(148),
    FX_COLOR(149),
    PATTERN_COLOR(150),
    PATTERN_AUTO_MODE(151),
    SONG_LOOP_POSITION(152),
    AU_SMP_RATE(153),
    FX_IN_CHANNEL_NUM(154),

    FINE_TEMPO(156),

    // Song metadata
    CHANNEL_NAME(192),
    PATTERN_NAME(193),
    SONG_TITLE(194),
    SONG_COMMENT(195),
    SAMPLE_FILENAME(196),
    SONG_URL(197),
    SONG_COMMENT_RTF(198),
    USED_FL_VERSION(199),

    TEXT_DATAPATH(202),
    TEXT_EFFECTCHANNAME(204),
    TEXT_GENRE(206),
    TEXT_AUTHOR(207),
    TEXT_MIDICTRLS(208),
    TEXT_DELAY(209),
    TEXT_TS404PARAMS(210),
    TEXT_DELAYLINE(211),

    // Plug-ins
    TEXT_PLUGIN(212),
    COLOR(128),
    TEXT_PLUGIN_NAME_DEFAULT(201),
    TEXT_PLUGIN_NAME(203),
    PLUGIN_ICON(155),

    TEXT_PLUGINPARAMS(213),

    UNKNOWN_32(32),
    UNKNOWN_35(35),
    UNKNOWN_36(36),
    UNKNOWN_37(37),
    UNKNOWN_38(38),
    UNKNOWN_39(39),
    UNKNOWN_40(40),
    UNKNOWN_95(95),
    UNKNOWN_98(98),
    UNKNOWN_99(99),
    UNKNOWN_100(100),
    UNKNOWN_157(157),
    UNKNOWN_158(158),
    UNKNOWN_159(159),
    UNKNOWN_164(164),
    UNKNOWN_200(200),
    UNKNOWN_214(214),
    UNKNOWN_216(216),
    UNKNOWN_221(221),
    UNKNOWN_225(225),
    UNKNOWN_226(226),
    UNKNOWN_228(228),
    UNKNOWN_229(229),
    UNKNOWN_235(235),
    UNKNOWN_236(236),
    UNKNOWN_238(238),
    UNKNOWN_241(241),
    UNKNOWN_254(254),

    TEXT_CHANPARAMS(215),
    TEXT_ENVLFOPARAMS(218),
    TEXT_BASICCHANPARAMS(219),
    TEXT_OLDFILTERPARAMS(220),
    TEXT_AUTOMATIONDATA(223),
    TEXT_PATTERNNOTES(224),
    TEXT_CHANGROUPNAME(231),
    TEXT_PLAYLISTITEMS(233),
    TEXT_PROJECT_TIME(237);

    private final long value;
    private final SubType subType;

    FLPEventType(int value) {
        this.value = value;
        this.subType = SubType.find(value);
    }

    public long getValue() {
        return value;
    }

    public SubType getSubType() {
        return subType;
    }

    private static final List<FLPEventType> IGNORED = Arrays.asList(
            UNKNOWN_98,
            UNKNOWN_225,
            UNKNOWN_238,
            UNKNOWN_235,
            UNKNOWN_236,
            FX_IN_CHANNEL_NUM,
            FX_OUT_CHANNEL_NUM,
            TEXT_PLUGINPARAMS,
            TEXT_PLAYLISTITEMS
    );

    public boolean isSkippable() {
        return IGNORED.contains(this);
    }

    public enum SubType {
        BYTE(0),
        WORD(64),
        INT(128),
        DATA(192);

        public final int offset;

        SubType(int offset) {
            this.offset = offset;
        }

        public static SubType find(int id) {
            if (id < WORD.offset) {
                return BYTE;
            } else if (id < INT.offset) {
                return WORD;
            } else if (id < DATA.offset) {
                return INT;
            } else return DATA;
        }
    }

    public static FLPEventType find(long id) {
        FLPEventType type = Arrays.stream(values()).filter(v -> v.value == id).findAny().orElse(UNKNOWN);
        if (type.equals(UNKNOWN)) {
            System.out.println("Unknown ID: " + id);
        }
        return type;
    }

}

