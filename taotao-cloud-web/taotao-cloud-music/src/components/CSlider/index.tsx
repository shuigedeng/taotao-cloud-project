import Taro, {FC, memo} from "@tarojs/taro";
import {Slider, View} from "@tarojs/components";

import "./index.less";

type Props = {
  percent: number;
  onChange: (object) => any;
  onChanging: (object) => any;
};

const CSlider: FC<Props> = ({percent, onChange, onChanging}) => {
  return (
    <View className="slider_components">
      <Slider
        value={percent}
        blockSize={15}
        activeColor="#d43c33"
        onChange={e => onChange(e)}
        onChanging={e => onChanging(e)}
      />
    </View>
  );
};

export default memo(CSlider, (prevProps, nextProps) => {
  return prevProps.percent === nextProps.percent;
});
