//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract DevFaucet is Ownable {
    bool public mintable;
    mapping(address => uint) public timestampOfLastMint;
    uint public balance;
    address public tokenAddress;
    uint timer;

    IERC20 public token;
    Ownable ownableToken;

    event SupplyChange(address mintedToAddress, uint amount);

    constructor(address _tokenAddress, uint mintTimer) {
        // only owner of token can deploy this faucet
        tokenAddress = _tokenAddress;
        token = IERC20(_tokenAddress);
        ownableToken = Ownable(address(token));
        require(ownableToken.owner() == msg.sender, "not owner of token");

        timer = mintTimer;
        mintable = true;
    }

    function distribute() public {
        require(mintable == true, "unable to mint right now");
        balance = token.balanceOf(address(this));
        require(
            block.timestamp >= timestampOfLastMint[msg.sender] + timer,
            "Cannot mint yet, still on cooldown."
        );
        require(msg.sender != address(0), "address not be 0x0");
        uint amount = 1000;
        token.approve(msg.sender, amount);
        token.transfer(msg.sender, amount);
        balance -= amount;
        timestampOfLastMint[msg.sender] = block.timestamp;

        emit SupplyChange(msg.sender, amount);
    }

    function toggleFaucetMintable() public onlyOwner {
        mintable = !mintable;
    }
}
