package com.dmgorbunov.frustaj.data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FLPData {

    private static final List<Message> KNOWN_MESSAGES;

    static {
        KNOWN_MESSAGES = Stream.of(Basic.values(), Text.values())
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

    private interface Message<T extends Enum<T>> {
        public int getValue();
    }

    private interface Block<T extends Enum<T>> {
        public <V> Function<byte[], V> getMappingFunction();
    }

    public enum Basic implements Message {

        // fallback value
        UNKNOWN(-1),

        // 1 byte values

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

        // 2 byte values
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

        // 4 byte values
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
        FX_OUT_CHAN_NUM(147),
        NEW_TIME_MARKER(148),
        FX_COLOR(149),
        PATTERN_COLOR(150),
        PATTERN_AUTO_MODE(151),
        SONG_LOOP_POSITION(152),
        AU_SMP_RATE(153),
        FX_IN_CHANNEL_NUM(154),
        FINE_TEMPO(156);


        final int value;

        Basic(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    public enum Text implements Message, Block {
        CHANNEL_NAME(192),
        PATTERN_NAME(193),
        SONG_TITLE(194),
        SONG_COMMENT(195),
        SAMPLE_FILENAME(196),
        SONG_URL(197),
        SONG_COMMENT_RTF(198),
        USED_FL_VERSION(199, StandardCharsets.UTF_8),

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
        TEXT_CHANPARAMS(215),
        TEXT_ENVLFOPARAMS(218),
        TEXT_BASICCHANPARAMS(219),
        TEXT_OLDFILTERPARAMS(220),
        TEXT_AUTOMATIONDATA(223),
        TEXT_PATTERNNOTES(224),
        TEXT_CHANGROUPNAME(231),
        TEXT_PLAYLISTITEMS(233),
        TEXT_PROJECT_TIME(237);

        final int value;
        final Function<byte[], String> mappingFunction;

        Text(int value, Function<byte[], String> mappingFunction) {
            this.value = value;
            this.mappingFunction = mappingFunction;
        }

        Text(int value, Charset charset) {
            this(value, b -> new String(b, charset));
        }

        Text(int value) {
            this(value, StandardCharsets.UTF_16LE);
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Function<byte[], String> getMappingFunction() {
            return mappingFunction;
        }
    }

    public static Message find(int id) {
        return KNOWN_MESSAGES.stream().filter(v -> id == v.getValue())
                .findAny().orElse(Basic.UNKNOWN);
    }
}
