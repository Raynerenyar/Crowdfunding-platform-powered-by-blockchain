//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/token/ERC20/presets/ERC20PresetMinterPauser.sol";

contract Token is IERC20, ERC20, ERC20PresetMinterPauser, Ownable {
    constructor() ERC20PresetMinterPauser("Development", "DEV") {}

    function _beforeTokenTransfer(
        address from,
        address to,
        uint256 amount
    ) internal view override(ERC20, ERC20PresetMinterPauser) {}
}
