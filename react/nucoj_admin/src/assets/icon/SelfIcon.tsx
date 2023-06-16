import Icon from '@ant-design/icons';
import type { CustomIconComponentProps } from '@ant-design/icons/lib/components/Icon';
import React from 'react';

const Svg = () => (
    <svg d="1667739660559" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
         p-id="11600" width="20" height="20">
        <path
            d="M512 1024C229.233778 1024 0 794.766222 0 512 0 229.233778 229.233778 0 512 0 794.766222 0 1024 229.233778 1024 512 1024 794.766222 794.766222 1024 512 1024ZM512 56.888889C260.664889 56.888889 56.888889 260.636444 56.888889 512 56.888889 763.363556 260.664889 967.111111 512 967.111111 763.363556 967.111111 967.111111 763.363556 967.111111 512 967.111111 260.636444 763.363556 56.888889 512 56.888889ZM853.333333 711.111111 796.444444 711.111111C796.444444 632.576 732.757333 568.888889 654.222222 568.888889 575.658667 568.888889 512 632.576 512 711.111111L455.111111 711.111111C455.111111 634.254222 498.716444 567.694222 562.460444 534.499556 531.854222 508.416 512 470.044444 512 426.666667 512 348.131556 575.658667 284.444444 654.222222 284.444444 732.757333 284.444444 796.444444 348.131556 796.444444 426.666667 796.444444 470.044444 776.618667 508.416 745.984 534.499556 809.728 567.694222 853.333333 634.254222 853.333333 711.111111ZM654.222222 341.333333C607.089778 341.333333 568.888889 379.534222 568.888889 426.666667 568.888889 473.799111 607.089778 512 654.222222 512 701.354667 512 739.555556 473.799111 739.555556 426.666667 739.555556 379.534222 701.354667 341.333333 654.222222 341.333333ZM199.111111 512 455.111111 512 455.111111 568.888889 199.111111 568.888889 199.111111 512ZM199.111111 369.777778 455.111111 369.777778 455.111111 426.666667 199.111111 426.666667 199.111111 369.777778ZM398.222222 711.111111 199.111111 711.111111 199.111111 654.222222 398.222222 654.222222 398.222222 711.111111Z"
            p-id="11601"></path>
    </svg>
);

/**
 * 个人图标
 * @param props
 * @constructor
 */
const SelfIcon = (props: Partial<CustomIconComponentProps>) => (
    <Icon component={Svg} {...props} />
);

export default SelfIcon
