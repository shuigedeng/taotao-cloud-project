import { IPluginContext } from "@tarojs/service";

const fs = require("fs-extra");
const path = require("path");

const getConfigPath = (platform) =>
  `${HIDDEN_CONFIG_PATH}/${platform}.config.js`;
const checkConfigExists = (platform) =>
  fs.existsSync(path.resolve(getConfigPath(platform)));

const HIDDEN_CONFIG_PATH = "config/tailwind";
const SUPPORTED_MINI_PLATFORMS = [
  "weapp",
  "swan",
  "alipay",
  "tt",
  "qq",
  "quickapp",
  "h5",
];
const SUPPORTED_PLATFORMS = ["h5", ...SUPPORTED_MINI_PLATFORMS];
const CURRENT_PLATFORM = process.env.TARO_ENV || "UNSUPPORTED";

interface ITaroPluginTailwindOptions {
  test?: RegExp;
}

export default (
  ctx: IPluginContext,
  { test = /\/src\/styles\/tailwind\.less$/ }: ITaroPluginTailwindOptions
) => {
  ctx.registerCommand({
    name: "tt-tailwind",
    optionsMap: {
      "--init": "generates necessary configs and tailwind.src.css",
    },
    synopsisList: [
      "taro tt-tailwind --init",
      "taro tt-tailwind --init weapp,dd,tt,swan",
    ],
    fn() {
      const {
        options: { init },
      } = ctx.runOpts;
      const defaultConfig = path.join(__dirname, `./config/mini.config.js`);
      let generatePlatforms = ["mini", "h5"];
      if (init && typeof init === "string" && init.trim()) {
        generatePlatforms = init.split(",");
      }
      generatePlatforms.map((platform) => {
        const filePath = `${HIDDEN_CONFIG_PATH}/${platform}.config.js`;
        const targetFile = path.resolve(filePath);
        if (fs.existsSync(targetFile)) {
          console.log(
            ctx.helper.chalk.redBright(
              `⚠️ [taro-plugin-tailwind] File ${filePath} exists!`
            )
          );
        } else {
          if (
            fs.existsSync(
              path.join(__dirname, `./config/${platform}.config.js`)
            )
          ) {
            fs.copySync(
              path.join(__dirname, `./config/${platform}.config.js`),
              targetFile
            );
          } else {
            fs.copySync(defaultConfig, targetFile);
          }
          console.log(
            ctx.helper.chalk.greenBright(
              `[taro-plugin-tailwind] File ${filePath} has been created.`
            )
          );
        }
      });
    },
  });
  ctx.modifyWebpackChain(({ chain }) => {
    if (!SUPPORTED_PLATFORMS.includes(process.env.TARO_ENV || "UNSUPPORTED")) {
      console.log(
        ctx.helper.chalk.yellowBright(
          `⚠️ [taro-plugin-tailwind]: platform ${CURRENT_PLATFORM} is not supported, auto skipping...`
        )
      );
      return;
    }
    if (!["h5", "mini"].some((platform) => checkConfigExists(platform))) {
      console.log(
        ctx.helper.chalk.yellowBright(
          `⚠️ [taro-plugin-tailwind]: required config (h5.config.js / mini.config.js) is missing, auto skipping...`
        )
      );
      return;
    }
    const postcssConfig = {
      mini: {
        plugins: [
          require("postcss-import")(),
          require("tailwindcss")({
            config: getConfigPath(
              checkConfigExists(CURRENT_PLATFORM) ? CURRENT_PLATFORM : "mini"
            ),
          }),
          require("postcss-discard-empty")(),
          require("postcss-unprefix")(),
          require("postcss-css-variables")(),
          require("postcss-preset-env")(),
        ],
      },
      h5: {
        plugins: [
          require("postcss-import")(),
          require("tailwindcss")({ config: getConfigPath("h5") }),
          require("postcss-preset-env")(),
          require("autoprefixer")(),
        ],
      },
    };
    chain.merge({
      module: {
        rule: {
          taroTailwindLoader: {
            test,
            use: [
              {
                loader: "postcss-loader",
                options:
                  postcssConfig[
                    SUPPORTED_MINI_PLATFORMS.includes(CURRENT_PLATFORM)
                      ? "mini"
                      : "h5"
                  ],
              },
            ],
          },
        },
      },
    });
  });
};
