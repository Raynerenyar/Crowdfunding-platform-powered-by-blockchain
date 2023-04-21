//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

// import "@openzeppelin/contracts/access/AccessControl.sol";
// import "./TWLV.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract twlvFaucet is Ownable {
    bool public mintable;
    mapping(address => uint) public timestampOfLastMint;
    uint public balance;
    address public tokenAddress;
    address public twlvOwner;
    uint timer;

    IERC20 public twlv;
    Ownable ownableToken;

    event SupplyChange(address mintedToAddress, uint amount);

    constructor(address _tokenAddress, uint mintTimer) {
        timer = mintTimer;
        mintable = true;
        tokenAddress = _tokenAddress;
        twlv = IERC20(_tokenAddress);
        ownableToken = Ownable(address(twlv));

        // only owner of twlv can deploy this faucet
        require(ownableToken.owner() == msg.sender, "not owner of twlv");
    }

    function distribute() public {
        require(mintable == true, "unable to mint right now");
        balance = twlv.balanceOf(address(this));
        // require(block.timestamp >= blockSinceLastMint[to] + 90000, Your last mint is less than 25 hours);
        require(
            block.timestamp >= timestampOfLastMint[msg.sender] + timer,
            "your last mint is less than 1000 seconds"
        );
        require(msg.sender != address(0), "address not be 0x0");
        uint amount = 1000;
        twlv.approve(msg.sender, amount);
        twlv.transfer(msg.sender, amount);
        balance -= amount;
        timestampOfLastMint[msg.sender] = block.timestamp;

        emit SupplyChange(msg.sender, amount);
    }

    function toggleFaucet() public onlyOwner {
        mintable = !mintable;
    }
}
