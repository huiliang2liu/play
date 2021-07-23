package com.xh.play.platforms;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;

import java.io.Serializable;
import java.util.List;

public interface IPlatforms extends Serializable {
    List<Title> main();

    ListMove list(String url);

    boolean playDetail(Detial detial);

    String play(Detial.DetailPlayUrl playUrl);

    List<Detial> search(String text);

    String name();
}
