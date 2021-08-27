package com.runtimeterror.rekindle;

import java.util.ArrayList;
import java.util.List;

public abstract class LeitnerBox {
    int threshold;
    int state;

    public LeitnerBox(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

class Box1 extends LeitnerBox {
      public Box1(int state) {
          super(state);
          this.threshold = Constants.BOX1_THRESHOLD;
      }
}

class Box2 extends LeitnerBox {
    public Box2(int state) {
        super(state);
        threshold = Constants.BOX2_THRESHOLD;
    }
}

class Box3 extends LeitnerBox {
    public Box3(int state) {
        super(state);
        threshold = Constants.BOX3_THRESHOLD;
    }
}
