import Icon from '@ant-design/icons';
import type { CustomIconComponentProps } from '@ant-design/icons/lib/components/Icon';
import React from 'react';

const UpdateSvg = () => (
    <svg d="1665021891927" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
         p-id="7706" width="26" height="26">
        <path
            d="M908.123 1023.8H115.477c-60.388 0-99.08-30.494-99.08-96.781v-766.35c0-53.39 38.692-100.38 99.08-100.38H254.15V20.095C254.15 8.998 263.05 0 273.946 0h19.797c10.897 0 19.796 8.998 19.796 20.096v40.192H709.96V20.096C709.961 8.998 718.86 0 729.757 0h19.797c10.997 0 19.796 8.998 19.796 20.096v40.192h118.877c60.388 0 118.876 46.991 118.876 100.38v766.35c0.1 53.39-38.592 96.782-98.98 96.782z m39.692-863.131c0-22.196-17.797-40.193-39.592-40.193H769.45v40.193c0 11.097-8.899 20.096-19.796 20.096h-19.797c-10.897 0-19.796-8.999-19.796-20.096v-40.193H313.74v40.193c0 11.097-8.899 20.096-19.796 20.096h-19.897c-10.997 0-19.796-8.999-19.796-20.096v-40.193H115.477c-21.895 0-39.592 17.997-39.592 40.193v100.38h871.93v-100.38z m0 160.568H75.885v582.187c2.4 46.49 17.497 60.188 49.99 60.188h771.85c27.694-1.7 51.79-15.097 49.99-60.188V321.237z m-464.71 467.809c-12.497 13.997-32.793 13.997-45.19 0L306.34 641.575c-12.497-13.998-12.497-36.693 0-50.69s32.694-13.998 45.191 0l108.88 122.176 211.658-237.254c12.497-13.997 32.693-13.997 45.19 0s12.498 36.693 0 50.69L483.107 789.046z m0 0"
            fill="#707070" p-id="7707"></path>
    </svg>
);

/**
 * 主页更新图标
 * @param props
 * @constructor
 */
const UpdateIcon = (props: Partial<CustomIconComponentProps>) => (
    <Icon component={UpdateSvg} {...props} />
);

export default UpdateIcon
