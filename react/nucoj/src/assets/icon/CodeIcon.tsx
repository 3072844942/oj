import Icon from '@ant-design/icons';
import type { CustomIconComponentProps } from '@ant-design/icons/lib/components/Icon';
import React from 'react';

const CodeSvg = () => (
    <svg d="1665811880513" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
         p-id="2579" width="32" height="32">
        <path
            d="M958.17 447.4L760.69 249.92l-65.82 65.83 197.47 197.47L694.87 710.7l65.82 65.82 197.48-197.47 65.83-65.83zM263.3 249.92L65.82 447.4 0 513.22l65.82 65.83L263.3 776.52l65.82-65.82-197.47-197.48 197.47-197.47zM343.247 949.483L590.96 52.19l89.72 24.768-247.713 897.295z"
            p-id="2580"></path>
    </svg>
);

/**
 * 排名图标
 * @param props
 * @constructor
 */
const CodeIcon = (props: Partial<CustomIconComponentProps>) => (
    <Icon component={CodeSvg} {...props} />
);

export default CodeIcon
