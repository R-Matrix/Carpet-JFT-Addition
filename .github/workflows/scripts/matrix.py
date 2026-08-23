#!/usr/bin/env python3
"""扫描 settings.json 中的版本列表, 生成 GitHub Actions include 矩阵.

可通过环境变量 TARGET_SUBPROJECT (逗号分隔) 过滤要构建的版本子项目.

本脚本的版本矩阵处理思路参考并改写自 Carpet-Igny-Addition，来源说明见
docs/acknowledgments.md。
"""
import json
import os
import sys


def main():
    target_env = os.environ.get('TARGET_SUBPROJECT', '')
    targets = [t.strip() for t in target_env.split(',') if t.strip()]

    with open('settings.json', encoding='utf-8') as f:
        settings = json.load(f)

    versions = settings['versions']
    if targets:
        missing = [t for t in targets if t not in versions]
        if missing:
            print('Unknown subprojects: {}'.format(missing), file=sys.stderr)
            sys.exit(1)
        versions = [v for v in versions if v in targets]

    matrix = {'include': [{'subproject': version} for version in versions]}
    with open(os.environ['GITHUB_OUTPUT'], 'a', encoding='utf-8') as f:
        f.write('matrix={}\n'.format(json.dumps(matrix)))


if __name__ == '__main__':
    main()
