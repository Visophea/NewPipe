package us.shandian.giga.postprocessing;

import org.schabi.newpipe.streams.WebMReader.TrackKind;
import org.schabi.newpipe.streams.WebMReader.WebMTrack;
import org.schabi.newpipe.streams.WebMWriter;
import org.schabi.newpipe.streams.io.SharpStream;

import java.io.IOException;

/**
 * @author kapodamy
 */
class WebMMuxer extends Postprocessing {

    WebMMuxer() {
        super(5 * 1024 * 1024/* 5 MiB */, true);
    }

    @Override
    int process(SharpStream out, SharpStream... sources) throws IOException {
        WebMWriter muxer = new WebMWriter(sources);
        muxer.parseSources();

        // youtube uses a webm with a fake video track that acts as a "cover image"
        WebMTrack[] tracks = muxer.getTracksFromSource(1);
        int audioTrackIndex = 0;
        for (int i = 0; i < tracks.length; i++) {
            if (tracks[i].kind == TrackKind.Audio) {
                audioTrackIndex = i;
                break;
            }
        }

        muxer.selectTracks(0, audioTrackIndex);
        muxer.build(out);

        return OK_RESULT;
    }

}
