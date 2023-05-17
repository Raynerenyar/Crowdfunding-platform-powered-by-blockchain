//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "./Crowdfunding.sol";

contract CrowdfundingFactory is Ownable {
    mapping(address => ProjectDetails) public projects;
    mapping(address => bool) public allowedAddresses;
    bool public isFreeForAll;

    struct ProjectDetails {
        string title;
        Crowdfunding project;
    }

    event CreateNewProjectEvent(
        address _projectAddress,
        address _projectCreator,
        string _title,
        uint _goal,
        uint _deadline,
        address _tokenUsed,
        string tokenName,
        string tokenSymbol
    );

    constructor() {
        isFreeForAll = true;
    }

    function createNewProject(
        uint _goal,
        uint _deadline,
        address _acceptingThisToken,
        string memory _title
    ) public {
        if (!isFreeForAll) {
            require(
                allowedAddresses[msg.sender] == true,
                "Address is not allowed"
            );
        }

        Crowdfunding crowdfunding = new Crowdfunding(
            _goal,
            _deadline,
            _acceptingThisToken,
            _title
        );

        require(address(crowdfunding) != address(0));
        crowdfunding.transferOwnership(msg.sender);

        ProjectDetails storage newProject = projects[address(crowdfunding)];

        newProject.title = _title;
        newProject.project = crowdfunding;
        IERC20Metadata token = IERC20Metadata(_acceptingThisToken);

        emit CreateNewProjectEvent(
            address(crowdfunding),
            msg.sender,
            _title,
            _goal,
            _deadline,
            _acceptingThisToken,
            token.name(),
            token.symbol()
        );
    }

    function allowAddress(address _allowThisAddress) public onlyOwner {
        require(allowedAddresses[_allowThisAddress] == false);
        allowedAddresses[_allowThisAddress] = true;
    }

    function disallowAddress(address _disallowThisAddress) public onlyOwner {
        require(allowedAddresses[_disallowThisAddress] == true);
        allowedAddresses[_disallowThisAddress] = false;
    }

    function toggleFreeForAll() public {
        isFreeForAll = !isFreeForAll;
    }

    function isContract(address _address) public view returns (bool) {
        uint size;
        assembly {
            size := extcodesize(_address)
        }
        return size > 0;
    }

    function isToken(address _address) public view returns (bool) {
        ERC20 testAddress = ERC20(_address);
        require(testAddress.balanceOf(msg.sender) >= 0, "method doesn't exist");
        require(testAddress.totalSupply() > 0, "Supply is 0");
        bytes memory _testString = bytes(testAddress.name());

        return _testString.length > 0;
    }
}
