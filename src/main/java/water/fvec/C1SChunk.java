package water.fvec;

import water.*;

// The scale/bias function, where data is in SIGNED bytes before scaling
public class C1SChunk extends Chunk {
  static private final long _NA = 0xFF;
  static final int OFF=8+4;
  double _scale;
  int _bias;
  C1SChunk( byte[] bs, int bias, double scale ) { _mem=bs; _start = -1; _len = _mem.length;
    _bias = bias; _scale = scale;
    UDP.set8d(_mem,0,scale);
    UDP.set4 (_mem,8,bias );
  }
  @Override public long   get ( int    i ) {
    long res = 0xFF&_mem[i+OFF];
    assert (res == _NA) || !_vec.isNA((long)((res+_bias)*_scale));
    return res == _NA?_vec._iNA:(long)((res+_bias)*_scale);
  }
  @Override public double getd( int    i ) {
    long res = 0xFF&_mem[i+OFF];
    assert res == _NA || !_vec.isNA((res+_bias)*_scale);
    return (res == _NA)?_vec._fNA:(res+_bias)*_scale;
  }
  @Override void   append2 ( long l, int exp ) { throw H2O.fail(); }
  @Override public AutoBuffer write(AutoBuffer bb) { return bb.putA1(_mem,_mem.length); }
  @Override public C1SChunk read(AutoBuffer bb) {
    _mem = bb.bufClose();
    _start = -1;
    _len = _mem.length;
    _scale= UDP.get8d(_mem,0);
    _bias = UDP.get4 (_mem,8);
    return this;
  }
}
