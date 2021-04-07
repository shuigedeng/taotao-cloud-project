import Taro, {FC, memo} from "@tarojs/taro";
import classnames from "classnames";
import {View} from "@tarojs/components";
import "./index.less";

type Props = {
  fullPage?: boolean;
  hide?: boolean;
};

const CLoading: FC<Props> = ({fullPage, hide}) => {
  const cls = classnames({
    loading_components: true,
    fullScreen: fullPage,
    hide: hide
  });
  return <View className={cls}/>;
};

export default memo(CLoading, (oldProps, newProps) => {
  return (
    oldProps.fullPage === newProps.fullPage && oldProps.hide === newProps.hide
  );
});
