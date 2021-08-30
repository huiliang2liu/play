package com.xh.paser;



import java.io.Serializable;
import java.util.List;

public interface IPlatform extends Serializable {
    List<Title> types();

    List<Title> titles(String url);

    String name();

    ListMove list(String url);

    boolean playDetail(Detial detial);

    String play(Detial.DetailPlayUrl playUrl);

    List<Detial> search(String text);

    boolean hasVip();

}
